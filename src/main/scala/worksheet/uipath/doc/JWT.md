# JWT (JSON Web Token)

## JSON Definition

JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.

Although JWTs can be encrypted to also provide secrecy between parties, we will focus on signed tokens. Signed tokens can verify the integrity of the claims contained within it, while encrypted tokens hide those claims from other parties. When tokens are signed using public/private key pairs, the signature also certifies that only the party holding the private key is the one that signed it.

## JSON Structure|anatomy

- Header,
- Payload,
- Signature.

so that JWT typically looks like the following.


Headers might look like:
> xxxx.yyyy.zzzz

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

## JWT usages

- Authorization
- Information Exchange

For HTTPs requests, JWTs are typically sent in the Authorization header using the Bearer schema. It should look like the following.

```http
http://example.com/

Content-Type: application/json
Accept: application/json
Authorization: Bearer <token>
```

## JWS (JSON Web Signature)


## References

- [JWT](https://jwt.io/introduction)
