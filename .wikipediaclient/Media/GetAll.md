```toml
name = 'GetAll'
method = 'GET'
url = '{{BaseUrl}}?action=query&format=json&prop=imageinfo&continue=grncontinue||&generator=random&formatversion=2&iiprop=timestamp|user|url|mime|canonicaltitle&grnlimit=20'
sortWeight = 16000000
id = '03a49b8f-df71-45ef-a2c6-d27fdce2c24d'

[[queryParams]]
key = 'action'
value = 'query'

[[queryParams]]
key = 'format'
value = 'json'

[[queryParams]]
key = 'prop'
value = 'imageinfo'

[[queryParams]]
key = 'continue'
value = 'grncontinue||'

[[queryParams]]
key = 'generator'
value = 'random'

[[queryParams]]
key = 'formatversion'
value = '2'

[[queryParams]]
key = 'iiprop'
value = 'timestamp|user|url|mime|canonicaltitle'

[[queryParams]]
key = 'grnlimit'
value = '20'

[[queryParams]]
key = 'grncontinue'
disabled = true
```
