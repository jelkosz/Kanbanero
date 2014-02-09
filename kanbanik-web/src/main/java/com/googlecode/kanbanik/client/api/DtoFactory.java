package com.googlecode.kanbanik.client.api;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.googlecode.kanbanik.client.security.CurrentUser;
import com.googlecode.kanbanik.dto.CommandNames;

import java.util.List;

public class DtoFactory {

    interface BeanFactory extends AutoBeanFactory {
        AutoBean<Dtos.LoginDto> loginDto();

        AutoBean<Dtos.SessionDto> sessionDto();

        AutoBean<Dtos.ErrorDto> errorDto();

        AutoBean<Dtos.UserDto> userDto();

        AutoBean<Dtos.StatusDto> statusDto();

        AutoBean<Dtos.UserManipulationDto> userManipulationDto();

        AutoBean<Dtos.EmptyDto> emptyDto();

        AutoBean<Dtos.UsersDto> usersDto();

        AutoBean<Dtos.ClassOfServiceDto> classOfServiceDto();

        AutoBean<Dtos.ClassOfServicesDto> classOfServicseDto();

        AutoBean<Dtos.ProjectDto> projectDto();

        AutoBean<Dtos.ProjectsDto> projectsDto();

        AutoBean<Dtos.ProjectWithBoardDto> projectWithBoardDto();

        AutoBean<Dtos.TaskDto> taskDto();

        AutoBean<Dtos.TasksDto> tasksDto();

        AutoBean<Dtos.MoveTaskDto> moveTaskDto();

    }

    private static final BeanFactory factory = GWT.create(BeanFactory.class);

    public static Dtos.LoginDto loginDto(String name, String password) {
        Dtos.LoginDto dto = factory.loginDto().as();
        dto.setCommandName(CommandNames.LOGIN.name);
        dto.setUserName(name);
        dto.setPassword(password);
        dto.setSessionId(CurrentUser.getInstance().getSessionId());
        return dto;
    }

    public static Dtos.ErrorDto errorDto() {
        return factory.errorDto().as();
    }

    public static Dtos.SessionDto sessionDto() {
        return sessionDto(CurrentUser.getInstance().getSessionId());
    }

    public static Dtos.SessionDto sessionDto(String sessionId) {
        Dtos.SessionDto dto = factory.sessionDto().as();
        dto.setSessionId(sessionId);
        return dto;
    }

    public static Dtos.StatusDto statusDto() {
        return factory.statusDto().as();
    }

    public static Dtos.UserManipulationDto userManipulationDto() {
        Dtos.UserManipulationDto dto = factory.userManipulationDto().as();
        dto.setSessionId(CurrentUser.getInstance().getSessionId());
        return dto;
    }

    public static Dtos.ClassOfServiceDto classOfServiceDto() {
        Dtos.ClassOfServiceDto dto = factory.classOfServiceDto().as();
        dto.setSessionId(CurrentUser.getInstance().getSessionId());
        return dto;
    }

    public static Dtos.ProjectDto projectDto() {
        Dtos.ProjectDto dto = factory.projectDto().as();
        dto.setSessionId(CurrentUser.getInstance().getSessionId());
        return dto;
    }

    public static Dtos.TaskDto taskDto() {
        Dtos.TaskDto dto = factory.taskDto().as();
        dto.setSessionId(CurrentUser.getInstance().getSessionId());
        return dto;
    }

    public static Dtos.TasksDto tasksDto(List<Dtos.TaskDto> values) {
        Dtos.TasksDto dto = factory.tasksDto().as();
        dto.setSessionId(CurrentUser.getInstance().getSessionId());
        dto.setValues(values);
        return dto;
    }

    public static Dtos.MoveTaskDto moveTaskDto() {
        Dtos.MoveTaskDto dto = factory.moveTaskDto().as();
        dto.setSessionId(CurrentUser.getInstance().getSessionId());
        return dto;
    }

    public static Dtos.ProjectWithBoardDto projectWithBoardDto() {
        Dtos.ProjectWithBoardDto dto = factory.projectWithBoardDto().as();
        dto.setSessionId(CurrentUser.getInstance().getSessionId());
        return dto;
    }

    public static Dtos.UsersDto usersDto() {
        return factory.usersDto().as();
    }

    public static Dtos.UserDto userDto() {
        Dtos.UserDto dto = factory.userDto().as();
        dto.setSessionId(CurrentUser.getInstance().getSessionId());
        return dto;
    }

    public static Dtos.EmptyDto emptyDto() {
        return factory.emptyDto().as();
    }

    public static <T> T asDto(Class<T> clazz, String json) {
        AutoBean<T> bean = AutoBeanCodex.decode(factory, clazz, json);
        return bean.as();
    }

    public static <T> String asJson(T dto) {
        AutoBean<T> bean = AutoBeanUtils.getAutoBean(dto);
        return "command="+ AutoBeanCodex.encode(bean).getPayload();
    }

}
