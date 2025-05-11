```toml
name = 'SearchCategoriesForPrefix'
method = 'GET'
url = '{{BaseUrl}}?action=query&format=json&formatversion=2&generator=allcategories&prop=categoryinfo|description|pageimages&piprop=thumbnail&pithumbsize=70&gacprefix=Neo&gaclimit=10&gacoffset=0'
sortWeight = 2000000
id = 'b1efa49c-5389-4841-a568-80b933243241'

[[queryParams]]
key = 'action'
value = 'query'

[[queryParams]]
key = 'format'
value = 'json'

[[queryParams]]
key = 'formatversion'
value = '2'

[[queryParams]]
key = 'generator'
value = 'allcategories'

[[queryParams]]
key = 'prop'
value = 'categoryinfo|description|pageimages'

[[queryParams]]
key = 'piprop'
value = 'thumbnail'

[[queryParams]]
key = 'pithumbsize'
value = '70'

[[queryParams]]
key = 'gacprefix'
value = 'Neo'

[[queryParams]]
key = 'gaclimit'
value = '10'

[[queryParams]]
key = 'gacoffset'
value = '0'
```
