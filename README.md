# Урок 9. Способы организации передачи данных между компонентами приложения, протоколы и API. REST, gRPC, очереди

Разработать экранные формы интерфейса для заказа ресурсов в облачном сервисе в https://www.figma.com/ или https://app.diagrams.net/.
Разработать полную ERD домена в https://www.dbdesigner.net/.
По желанию - Добавить swagger, дополнить ответами домена (сутевые ответы) о статусе заказа ресурсов (создан, ошибка, нет ответа) и смоделировать ошибки REST «400, 500» типов.
Имплементировать сгенерированный swagger код в приложения студента.

Сначала добавьте зависимость на библиотеку Swagger для создания Swagger-спецификации:

<dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-annotations</artifactId>
    <version>2.1.10</version>
</dependency>

Создайте классы, представляющие ваш API, и используйте аннотации Swagger для описания этих классов и методов. Например:

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api(value = "/users", tags = "Users")
public class UsersController {
    private UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get a list of users")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of user list"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @ApiOperation(value = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "User created successfully"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @ApiOperation(value = "Delete a user by ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "User deleted successfully"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response deleteUser(int userId) {
        return userService.deleteUser(userId);
    }
}
В этом примере, мы используем аннотации Swagger для описания операций API и возможных кодов ответов.

В этом примере, мы используем аннотации Swagger для описания операций API и возможных кодов ответов.

Генерация Swagger JSON:

Для генерации Swagger JSON из ваших классов, вы можете использовать инструмент, такой как Swagger Core. С помощью Swagger Core, вы можете создать Swagger-спецификацию, которую вы добавите к вашему проекту.

Для генерации Swagger JSON, вы можете использовать командную строку или плагин Maven/Gradle, если вы используете их в вашем проекте.

Добавление Swagger UI:

Вы также можете включить Swagger UI, чтобы визуально просматривать и тестировать ваше API. Для этого, скопируйте Swagger UI в ваш проект и настройте соответствующий путь к нему.

Пример Swagger UI: https://github.com/swagger-api/swagger-ui

Запуск сервера:

Запустите ваш HTTP-сервер, который обрабатывает запросы, и ваш Swagger-документированный API будет доступен для тестирования через Swagger UI.

Обратите внимание, что это общий пример, и настройка Swagger может варьироваться в зависимости от вашего проекта и используемых инструментов. Вы должны также удостовериться, что ваш HTTP-сервер правильно настроен для обработки запросов и передачи Swagger-спецификации.