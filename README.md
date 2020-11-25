# Simple Scrapping Program for icd11 codes from WHO  

Sample output after the scraping ends

````json{
  "data": [
    {
      "code": "RA01.0",
      "typeId": 160,
      "name": "COVID-19, virus identified",
      "link": "http://id.who.int/icd/entity/1790791774",
      "type": {
        "subcategoryId": 13,
        "name": "RA01.0 COVID-19, virus identified",
        "link": "http://id.who.int/icd/entity/1790791774",
        "subcategory": {
          "categoryId": 1,
          "name": "Certain zoonotic viral diseases",
          "link": "http://id.who.int/icd/entity/1251496839",
          "category": {
            "name": "01 Certain infectious or parasitic diseases",
            "link": "http://id.who.int/icd/entity/1435254666"
          }
        }
      }
    },
    {
      "code": "RA01.0",
      "typeId": 2364,
      "name": "COVID-19, virus identified",
      "link": "http://id.who.int/icd/entity/1790791774",
      "type": {
        "subcategoryId": 331,
        "name": "RA01 COVID-19",
        "link": "http://id.who.int/icd/entity/1730556128",
        "subcategory": {
          "categoryId": 25,
          "name": "International provisional assignment of new diseases of uncertain aetiology and emergency use",
          "link": "http://id.who.int/icd/entity/486488173",
          "category": {
            "name": "25 Codes for special purposes",
            "link": "http://id.who.int/icd/entity/1596590595"
          }
        }
      }
    }
  ]
}
