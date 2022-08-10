# 生成keystore
```shell
openssl genrsa -des3 -passout pass:x -out server.pass.key 2048
openssl rsa -passin pass:x -in server.pass.key -out server.key
openssl req -new -key server.key -out server.csr
openssl x509 -req -sha256 -extfile v3.ext -days 365 -in server.csr -signkey server.key -out server.crt
openssl pkcs12 -export -name servercert -in server.crt -inkey server.key -out myp12keystore.p12
keytool -importkeystore -destkeystore mykeystore.jks -srckeystore myp12keystore.p12 -srcstoretype pkcs12 -alias servercert
keytool -list -rfc --keystore mykeystore.jks | openssl x509 -inform pem -pubkey -noout
```