```toml
name = 'Login2FA'
method = 'POST'
url = '{{BaseUrl}}?format=json&formatversion=2&errorformat=plaintext&action=clientlogin'
sortWeight = 6000000
id = 'ab70dd30-308c-4a8b-b48b-17d2a293dc4f'

[[queryParams]]
key = 'logintoken'
value = '654db5342b0d09c8b6db6883890d3acc67f843ac+\'
disabled = true

[[queryParams]]
key = 'username'
value = 'mshdabiola'
disabled = true

[[queryParams]]
key = 'password'
value = 'R3ZN8ZTMJU%23GFRb'
disabled = true

[[queryParams]]
key = 'rememberMe'
value = ''
disabled = true

[[queryParams]]
key = 'format'
value = 'json'

[[queryParams]]
key = 'formatversion'
value = '2'

[[queryParams]]
key = 'errorformat'
value = 'plaintext'

[[queryParams]]
key = 'action'
value = 'clientlogin'

[[queryParams]]
key = 'rememberMe'
disabled = true

[[headers]]
key = 'Cache-Control'
value = 'no-cache'

[[body.urlEncoded]]
key = 'username'
value = 'Mshdabiola'

[[body.urlEncoded]]
key = 'password'
value = 'R3ZN8ZTMJU#GFRb'

[[body.urlEncoded]]
key = 'logintoken'
value = '1f48adbddc0d477bcb08529e6e91563967f8be43+\'

[[body.urlEncoded]]
key = 'uselang'
value = 'en'

[[body.urlEncoded]]
key = 'loginreturnurl'
value = 'http://abiol.com'
```
