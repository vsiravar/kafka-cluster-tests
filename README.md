### Hello, kafka cert explorer

This project explores the certificate used by the kafka broker for SSL. It prints out the expiry date of the certificate along with the time remaining before it's invalid.

### Setup
1. We setup kafka cluster using docker compose. Start the cluster using `docker compose up`. The brokers use self signed certificate for SSL just for testing purposes. The certifactes are available under `ssl` folder.
2. Create self-signed certificate for by running `./scripts/create_certs.sh`. Choose any passphrase but when prompted for keystore password use `kafka1234`. See docker-compose.yml for more details. 
3. `cd certificate_explorer; mvn clean install`
