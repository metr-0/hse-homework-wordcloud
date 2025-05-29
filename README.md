## Запуск: `docker-compose up -d`
## Проверить тесты: `./test-all.sh`
## Посмотреть доки и потыкать API: http://localhost:8080

### На всякий случай и сюда небольшое описание API:

`[POST] /files` - загрузить файл, вернёт id

`[GET] /files` - вернёт список всех id

`[GET] /files/{id}` - вернёт файлик по id

`[DELETE] /files/{id}` - удалит файлик по id

`[POST] /analysis/file/{fileId}` - сделает облако по id файла и вернёт название картинки

`[GET] /analysis/cloud/{location}` - вернёт уже сделанную картинку облака по названию

`[GET] /health` - пинг-понг
