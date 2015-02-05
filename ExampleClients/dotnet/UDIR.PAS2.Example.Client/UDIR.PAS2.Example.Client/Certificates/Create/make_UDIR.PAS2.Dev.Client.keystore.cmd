
openssl pkcs12 -export -out UDIR.PAS2.Dev.Client.p12 -password pass:123456 -inkey UDIR.PAS2.Dev.Client.key -in UDIR.PAS2.Dev.Client.crt -certfile UDIR.PAS2.Dev.RootCA.crt
keytool.exe -importkeystore -destkeystore UDIR.PAS2.keystore -deststorepass 123456 -srckeystore UDIR.PAS2.Dev.Client.p12 -srcstoretype PKCS12 -srcstorepass 123456
