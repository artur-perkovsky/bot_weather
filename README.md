# Telegram Bot Weather

Данный телеграм бот позволяет отображать текущие погодные условие городов пользователя.
Для простоты использования бота города сохраняются и нет необходимости вводить каждый раз нужный город. Так же имеется возможность удалять сохранённые города.
Есть возможность получить погодные условия множетсв городов.

***
## Требования
Список инструментов, необходимых для сборки и запуска проекта:
- Jdk17
- Spring Boot 3
- Maven 4.0.0
- Webhook
- Postgres SQL 42.2.5
- Ngrok

***
## Запуск приложения

#### База данных
Для старата приложение необходимо запустить базу данных Postgres SQL.
В данном пректе - Postgres на порту - 5432, название базы данных - bot_weather.
#### Ngrock
Так как Telegram API работатет только с ssl сертификатоми, будем использывать ngrock proxy. Регистрируемся, получаем токен, стартуем proxy.
После запуска ngrock необходимо в конфигурацию добавить токен командой:
```
ngrok config add-authtoken "token"
```
"token" - сгенерированный в личном кабинете.
Далее необходимо запустить команду "http" и указать адрес ретрансляции запросов.
```
ngrok http http://localhost:8080
```
После старта ngrok он предоставит ссылку "https" которая будет ретранслирвать запросы на наш сервер.
#### Telegram API
Для работы с Telegram API необходимо получить уникальный индетификатор бота (токен). Для этого необходимо перейти в Telegram, зайти в канал BotFather и там создйте нового бота, и получить токен.
[Как создать бота можно прочесть сдесь](https://sendpulse.com/knowledge-base/chatbot/telegram/create-telegram-chatbot)
И указать токен, имя бота и адрес сервера в классе конфигурации.
#### Регистрция Webhook
В данном проекте метод взаимодествие с telegram api будем осуществлять через webhook. Для этого необходимо зарегистрировать webhook, для этого необходимо перейти по ссылке
```https
https://api.telegram.org/bot{token}/setWebhook?url={ngrok}
```
{token} - telegram token полученный BotFather
{ngrok} - адрес "https://....." полученный от ngrock
#### Weather API
Для получения прогноза погоды воспользуемся [API](www.weatherapi.com)
Проходим регистрацию, генерируем токен, этот токет указываем в конфигурациооном класе API.
#### Конфигурационный файл
В конфигурационном файле (application.properties) используется переменные среды.
```java
server.port=8080  
  
bot.name=@MyWeatherMapbot  
bot.token=${TELEGRAM_BOT_WEATHER_TOKEN}  
bot.path=https://feb3-37-212-86-241.ngrok-free.app  
  
spring.datasource.url=jdbc:postgresql://localhost:5432/bot_weather  
spring.datasource.username=${POSTGRES_USERNAME}  
spring.datasource.password=${POSTGRES_PASSWORD}  
  
spring.jpa.show-sql=true  
spring.datasource.driver-class-name=org.postgresql.Driver  
spring.jpa.hibernate.ddl-auto=update  
  
api.token=${API_WEATHER_TOKEN}
```
"bot.path"  - адрес сконфигурированный ngrock  .

#### Зависимости
```xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-web</artifactId>  
</dependency>  
  
<dependency>  
    <groupId>org.projectlombok</groupId>  
    <artifactId>lombok</artifactId>  
    <optional>true</optional>  
</dependency>  
  
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-test</artifactId> 
    <scope>test</scope>  
</dependency>  
  
<dependency>  
    <groupId>org.telegram</groupId>  
    <artifactId>telegrambots</artifactId>  
    <version>6.7.0</version>  
</dependency>  
<dependency>  
    <groupId>org.projectlombok</groupId>  
    <artifactId>lombok-maven-plugin</artifactId>  
    <version>1.18.20.0</version>  
    <scope>provided</scope>  
</dependency>  
  
<dependency>  
    <groupId>ch.qos.logback</groupId>  
    <artifactId>logback-classic</artifactId>  
</dependency>  
  
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-data-jpa</artifactId>  
</dependency>  
<dependency>  
    <groupId>org.postgresql</groupId>  
    <artifactId>postgresql</artifactId>  
    <version>42.2.5</version>  
</dependency>  
  
<dependency>  
    <groupId>javax.xml.bind</groupId>  
    <artifactId>jaxb-api</artifactId>  
    <version>2.3.0</version>  
</dependency>  
  
<dependency>  
    <groupId>org.jetbrains</groupId>  
    <artifactId>annotations</artifactId>  
    <version>RELEASE</version>  
    <scope>compile</scope>  
</dependency>
```

***
## Автор

Copyright © - 2024,  Perkovsky Artur

***

