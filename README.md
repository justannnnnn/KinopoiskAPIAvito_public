<i>Инструкция по запуску приложения</i>

Для того, чтобы работа с сервером была осуществлена, необходимо в файле Constants.kt заполнить поле <b>X_API_KEY</b>.

Примеры запросов:

--Получение списка всех фильмов
curl --request GET \
     --url 'https://api.kinopoisk.dev/v1.4/movie?page=1&limit=10' \
     --header 'X-API-KEY: --/--/--/--' \
     --header 'accept: application/json'

--Получение списка фильмов по названию
curl --request GET \
     --url 'https://api.kinopoisk.dev/v1.4/movie/search?page=1&limit=10&query=%D0%BC%D1%81%D1%82' \
     --header 'X-API-KEY: --/--/--/--' \
     --header 'accept: application/json'

--Получение списка съемочной группы по фильму
curl --request GET \
     --url 'https://api.kinopoisk.dev/v1.4/person?page=1&limit=10&movies.id=55576' \
     --header 'X-API-KEY: --/--/--/--' \
     --header 'accept: application/json'
