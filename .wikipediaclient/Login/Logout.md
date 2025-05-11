```toml
name = 'Logout'
method = 'POST'
url = 'https://commons.wikimedia.org/w/api.php?action=logout&format=json&formatversion=2'
sortWeight = 4000000
id = '34efcb24-ff8e-43bb-9676-c5d160de8e31'

[[queryParams]]
key = 'action'
value = 'logout'

[[queryParams]]
key = 'format'
value = 'json'

[[queryParams]]
key = 'token'
value = '9d45e71d999ff13e8423ec453f34934a67f8bfbe+\'
disabled = true

[[queryParams]]
key = 'formatversion'
value = '2'

[[body.urlEncoded]]
key = 'token'
value = '9d45e71d999ff13e8423ec453f34934a67f8bfbe+\'
```
