# Notes to be completed for Assignment 4 (part of the assessment)

## Step 1

A: Passphrase for ca-private.pem:Minmin


B: Password for ca-cert.jks:Minmin306


C: The CA's certificate fingerprint (SHA256) in ca-cert.jks:
SHA256: 5D:A6:58:2D:ED:6B:F5:78:BA:B8:F5:FF:62:C9:60:0C:55:1B:17:08:51:24:44:67:4C:6D:94:9C:BB:38:9E:49

## Step 2

D: Passphrase for server.jks:Minmin3061


E: The CA's certificate fingerprint (SHA256) in server.jks:

lab-rg06-05.cms.waikato.ac.nz, 6/10/2024, PrivateKeyEntry, 
Certificate fingerprint (SHA-256): 90:83:51:C6:34:E6:53:55:5A:A9:B2:08:E7:6F:F5:87:D5:B9:3B:48:60:9F:F7:6B:62:57:0D:FB:06:70:D8:C9
root, 6/10/2024, trustedCertEntry, 
Certificate fingerprint (SHA-256): 5D:A6:58:2D:ED:6B:F5:78:BA:B8:F5:FF:62:C9:60:0C:55:1B:17:08:51:24:44:67:4C:6D:94:9C:BB:38:9E:49


## Step 4

F: The exception from the first command:
mh1155@lab-rg06-05:~/COMPX204/Assigmnent_4$ java MyTLSFileClient localhost 50202 cat.png
javax.net.ssl.SSLHandshakeException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:131)
	at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:378)
	at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:321)
	at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:316)
	at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.checkServerCerts(CertificateMessage.java:1351)
	at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.onConsumeCertificate(CertificateMessage.java:1226)
	at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.consume(CertificateMessage.java:1169)
	at java.base/sun.security.ssl.SSLHandshake.consume(SSLHandshake.java:396)
	at java.base/sun.security.ssl.HandshakeContext.dispatch(HandshakeContext.java:480)
	at java.base/sun.security.ssl.HandshakeContext.dispatch(HandshakeContext.java:458)
	at java.base/sun.security.ssl.TransportContext.dispatch(TransportContext.java:201)
	at java.base/sun.security.ssl.SSLTransport.decode(SSLTransport.java:172)
	at java.base/sun.security.ssl.SSLSocketImpl.decode(SSLSocketImpl.java:1510)
	at java.base/sun.security.ssl.SSLSocketImpl.readHandshakeRecord(SSLSocketImpl.java:1425)
	at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:455)
	at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:426)
	at MyTLSFileClient.main(MyTLSFileClient.java:46)
Caused by: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at java.base/sun.security.validator.PKIXValidator.doBuild(PKIXValidator.java:439)
	at java.base/sun.security.validator.PKIXValidator.engineValidate(PKIXValidator.java:306)
	at java.base/sun.security.validator.Validator.validate(Validator.java:264)
	at java.base/sun.security.ssl.X509TrustManagerImpl.checkTrusted(X509TrustManagerImpl.java:231)
	at java.base/sun.security.ssl.X509TrustManagerImpl.checkServerTrusted(X509TrustManagerImpl.java:132)
	at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.checkServerCerts(CertificateMessage.java:1335)
	... 12 more
Caused by: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at java.base/sun.security.provider.certpath.SunCertPathBuilder.build(SunCertPathBuilder.java:148)
	at java.base/sun.security.provider.certpath.SunCertPathBuilder.engineBuild(SunCertPathBuilder.java:129)
	at java.base/java.security.cert.CertPathBuilder.build(CertPathBuilder.java:297)
	at java.base/sun.security.validator.PKIXValidator.doBuild(PKIXValidator.java:434)
	... 17 more



G: The exception from the second command:
mh1155@lab-rg06-05:~/COMPX204/Assigmnent_4$ java MyTLSFileClient localhost 50202 cat.png
javax.net.ssl.SSLHandshakeException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:131)
	at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:378)
	at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:321)
	at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:316)
	at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.checkServerCerts(CertificateMessage.java:1351)
	at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.onConsumeCertificate(CertificateMessage.java:1226)
	at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.consume(CertificateMessage.java:1169)
	at java.base/sun.security.ssl.SSLHandshake.consume(SSLHandshake.java:396)
	at java.base/sun.security.ssl.HandshakeContext.dispatch(HandshakeContext.java:480)
	at java.base/sun.security.ssl.HandshakeContext.dispatch(HandshakeContext.java:458)
	at java.base/sun.security.ssl.TransportContext.dispatch(TransportContext.java:201)
	at java.base/sun.security.ssl.SSLTransport.decode(SSLTransport.java:172)
	at java.base/sun.security.ssl.SSLSocketImpl.decode(SSLSocketImpl.java:1510)
	at java.base/sun.security.ssl.SSLSocketImpl.readHandshakeRecord(SSLSocketImpl.java:1425)
	at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:455)
	at java.base/sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:426)
	at MyTLSFileClient.main(MyTLSFileClient.java:46)
Caused by: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at java.base/sun.security.validator.PKIXValidator.doBuild(PKIXValidator.java:439)
	at java.base/sun.security.validator.PKIXValidator.engineValidate(PKIXValidator.java:306)
	at java.base/sun.security.validator.Validator.validate(Validator.java:264)
	at java.base/sun.security.ssl.X509TrustManagerImpl.checkTrusted(X509TrustManagerImpl.java:231)
	at java.base/sun.security.ssl.X509TrustManagerImpl.checkServerTrusted(X509TrustManagerImpl.java:132)
	at java.base/sun.security.ssl.CertificateMessage$T13CertificateConsumer.checkServerCerts(CertificateMessage.java:1335)
	... 12 more
Caused by: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at java.base/sun.security.provider.certpath.SunCertPathBuilder.build(SunCertPathBuilder.java:148)
	at java.base/sun.security.provider.certpath.SunCertPathBuilder.engineBuild(SunCertPathBuilder.java:129)
	at java.base/java.security.cert.CertPathBuilder.build(CertPathBuilder.java:297)
	at java.base/sun.security.validator.PKIXValidator.doBuild(PKIXValidator.java:434)
	... 17 more


