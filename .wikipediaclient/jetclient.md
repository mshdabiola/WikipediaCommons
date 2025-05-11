```toml
name = 'Wikipedia '
id = '19a8ff04-44ab-4db9-bf0a-080bcbd678cb'

[[environmentGroups]]
name = 'Default'
environments = ['Prod', 'Demo']
```

#### Variables

```json5
{
  globals: {
    BaseUrl : "{{host}}",
    WikiDataUrl : "https://www.wikidata.orgw/api.php",
    MW_API_PREFIX:  "?format=json&formatversion=2&errorformat=plaintext&",
    WikiDataUrlWithData : "{{WikiDataUrl}}/{{MW_API_PREFIX}}"
  },
  prod: {
    host : "https://commons.wikimedia.org/w/api.php"
  },
  demo: {
    host : "https://commons.wikimedia.beta.wmflabs.org"
  }
 
  
}
```
