# Для запуска docker необходимо создать сеть
```bash
docker network create --subnet=10.33.0.0/16 dev-patterns-network
```
# Запуск
```bash
docker compose up --build
```