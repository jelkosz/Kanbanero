package com.googlecode.kanbanik.client.messaging.messages.task;

import com.googlecode.kanbanik.client.messaging.BaseMessage;
import static com.googlecode.kanbanik.client.api.Dtos.TaskDto;

public class TaskAddedMessage extends BaseMessage<TaskDto> {

	public TaskAddedMessage(TaskDto payload, Object source) {
		super(payload, source);
	}

}
