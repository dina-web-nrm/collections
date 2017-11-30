#!/bin/bash
# able to check the TOKEN here -> https://jwt.io/ 
source .env

if [ ! -f .env ]; then
  echo "You need to create the file .env. DO:  'cp template.env .env' and add credentials"
  exit 1
fi 

dt=$(date '+%Y-%m-%d %H:%M:%S');

KEYCLOAK_SERVER=https://alpha-keycloak.dina-web.net/auth/realms/dina/protocol/openid-connect/token
#KEYCLOAK_SERVER=https://alpha-cm.dina-web.net/auth/realms/dina/protocol/openid-connect/token ## testing, anton has a 'routing'
ALPHA_SERVER=https://alpha-api.dina-web.net

RESULT=`curl --data "grant_type=password&client_id=${client_id}&username=${username}&password=${password}" $KEYCLOAK_SERVER`
TOKEN=`echo $RESULT | sed 's/.*access_token":"//g' | sed 's/".*//g'`

# able to pass in '-v' for verbose 
if [ $1 = "-v" ]; then
   RESPONSE=`curl -d '{
    "data": {
        "type": "individualGroup",
        "attributes": {
            "featureObservations": [{
                "featureObservationText": "This is a test",
                "featureObservationType": {
                    "name": "Test"
                }
            }],
            "occurrences": [{
                "collectorsText": "Ida Li",
                "localityText": "Sollentuna, Sweden",
                "occurrenceDateText": "2017-10-12"
            }],
            "physicalUnits": [{
                "physicalUnitText": "This is a test",
                "normalStorageLocation": "Stockholm",
                "catalogedUnit": {}
            }],
            "identifications": [{
                "identificationText": "This is a test"
            }]
        },
        "additionalData": [{
            "type": "catalogedUnit",
            "attributes": {
                "catalogNumber": "abc123"
            }
        }]
    }
}' -vH "Authorization: bearer $TOKEN" ${ALPHA_SERVER}/collections/api/v01/individualGroups -H "Content-Type: application/json"`

else
  RESPONSE=`curl -d '{
    "data": {
        "type": "individualGroup",
        "attributes": {
            "featureObservations": [{
                "featureObservationText": "This is a test",
                "featureObservationType": {
                    "name": "Test"
                }
            }],
            "occurrences": [{
                "collectorsText": "Ida Li",
                "localityText": "Sollentuna, Sweden",
                "occurrenceDateText": "2017-10-12"
            }],
            "physicalUnits": [{
                "physicalUnitText": "This is a test",
                "normalStorageLocation": "Stockholm",
                "catalogedUnit": {}
            }],
            "identifications": [{
                "identificationText": "This is a test"
            }]
        },
        "additionalData": [{
            "type": "catalogedUnit",
            "attributes": {
                "catalogNumber": "abc123"
            }
        }]
    }
}' -H "Authorization: bearer $TOKEN" ${ALPHA_SERVER}/collections/api/v01/individualGroups -H "Content-Type: application/json"`
fi

#echo $TOKEN

# writing to a log file
echo "$dt">> script.log
echo "Token is fetched from $KEYCLOAK_SERVER" >> script.log
echo "POST-Response from the ${ALPHA_SERVER} is \"$RESPONSE\" " >>script.log
echo "" >>script.log

echo $RESPONSE >>tmp.log
if grep --quiet "data" tmp.log; then
  echo "TRUE : correct response"
else
  echo FALSE: wrong response
fi

rm tmp.log
exit 0
