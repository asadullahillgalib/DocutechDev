package io.naztech.nuxeoclient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.nazdaqTechnologies.core.message.Message;
import com.nazdaqTechnologies.core.message.MessageContentType;
import com.nazdaqTechnologies.core.message.MessageHeader;
import com.nazdaqTechnologies.core.message.model.Status;
import com.nazdaqTechnologies.core.message.model.StatusType;
import com.nazdaqTechnologies.core.message.processor.json.gson.GsonJsonMessageProcessor;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.service.ServiceCoordinator;

/**
 * Controller for all HTTP/HTTPS actions and process through service classes
 * 
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-10-17
 */

@RestController
@RequestMapping(Constants.DOCUTECH)
public class Controller {
	private static Logger log = LoggerFactory.getLogger(Controller.class);

	@Autowired
	GsonJsonMessageProcessor messageProcessor;

	@Autowired
	ServiceCoordinator serviceCoordinator;

	@PostMapping(Constants.JSON_REQUEST)
	public String handleJsonRequest(@RequestBody String json) {

		Message<?> request = null;

		MessageHeader requestHeaders = null;

		String responseString = null;

		Message<?> dataMsg = null;
		Message<?> response = null;

		String errorString = null;

		String serviceName = null;

		Map<String, Object> statusMsgHeader = new HashMap<String, Object>();

		statusMsgHeader.put(MessageHeader.CONTENT_TYPE, MessageContentType.STATUS);

		try {
			request = messageProcessor.processMessage(json);

			log.debug("Recieved Request {}", json);

			if (request != null && request.getHeader().getContentType() != MessageContentType.EXCEPTION.toString()) {
				requestHeaders = request.getHeader();

				serviceName = requestHeaders.getDestination();

				if (serviceName != null) {

					log.debug("Source [{}] Destination [{}]", requestHeaders.getSource(), serviceName);

					/**
					 * @author mahbub.hasan
					 * validating request
					 *  */
					validateRequest(requestHeaders, request);

					/**
					 * @author mahbub.hasan
					 * sending message to service coordinator
					 * */
					dataMsg = serviceCoordinator.service(request);

					if (dataMsg == null) {
						response = handleErrResponse(request);
					} else {

						log.debug("Response string:{}", new Gson().toJson(dataMsg.getPayload()));

						response = handleSuccessResponse(request, dataMsg);
					}
				}
			}
		} catch (Exception ex) {
			log.error("error with request {}", ex);
			errorString = ex.getLocalizedMessage();

			List<Status> statusList = new ArrayList<Status>();
			statusList.add(new Status(StatusType.ERROR, errorString));
			response = messageProcessor.createResponseMessage(request, statusList, statusMsgHeader);
		}

		responseString = messageProcessor.toJson(response);

		return responseString;
	}

	/**
	 * @author mahbub.hasan
	 *  validates the http json request
	 *  */
	private void validateRequest(MessageHeader requestHeaders, Message<?> msg) throws Exception {

		StringBuffer sb = new StringBuffer();

		if (requestHeaders.getContentType() == null) {
			sb.append("Missing ContentType");
		}

		if (requestHeaders.getActionType() == null) {
			sb.append("Missing ActionType");
		}

		if (sb.length() > 0) {
			throw new Exception(sb.toString());
		}
	}

	/**
	 * @author mahbub.hasan
	 * handles the error response when there no response from service classes 
	 * */
	private Message<?> handleErrResponse(Message<?> request) {

		String serviceName = null;

		Message<?> response = null;

		Map<String, Object> statusMsgHeader = new HashMap<String, Object>();
		statusMsgHeader.put(MessageHeader.CONTENT_TYPE, MessageContentType.STATUS);

		try {
			String errorMsg = "no response received from service -> " + serviceName;
			log.error(errorMsg);
			List<Status> statusList = new ArrayList<Status>();
			statusList.add(new Status(StatusType.ERROR, errorMsg));

			response = messageProcessor.createResponseMessage(request, statusMsgHeader, statusMsgHeader);
		} catch (Exception e) {
			log.error("Caught Error {} / {}", e, e);
		}

		return response;
	}

	/**
	 * @author mahbub.hasan
	 * 
	 * handles the success responses from service classes
	 * 
	 * @param Message request and Message data 
	 * */
	private Message<?> handleSuccessResponse(Message<?> request, Message<?> dataMsg) {

		Message<?> response = null;

		List<Status> statusMsgList = new ArrayList<Status>();
		statusMsgList.add(new Status(StatusType.OK));

		Map<String, Object> statusMsgHeader = new HashMap<String, Object>();
		statusMsgHeader.put(MessageHeader.CONTENT_TYPE, MessageContentType.STATUS);

		Message<List<Status>> statusMsg = messageProcessor.createResponseMessage(request, statusMsgList,
				statusMsgHeader);

		List<Message<?>> msgBody = new ArrayList<Message<?>>();
		msgBody.add(statusMsg);
		msgBody.add(dataMsg);

		Map<String, Object> finalMsgHeader = new HashMap<String, Object>();
		finalMsgHeader.put(MessageHeader.CONTENT_TYPE, MessageContentType.MULTI);

		response = messageProcessor.createResponseMessage(request, msgBody, finalMsgHeader);

		return response;
	}
}
