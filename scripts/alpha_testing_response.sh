#!/bin/bash
source .env

KEYCLOAK_SERVER=https://alpha-keycloak.dina-web.net/auth/realms/dina/protocol/openid-connect/token
ALPHA_SERVER=https://alpha-api.dina-web.net

RESULT=`curl --data "grant_type=password&client_id=${client_id}&username=${username}&password=${password}" $KEYCLOAK_SERVER`
TOKEN=`echo $RESULT | sed 's/.*access_token":"//g' | sed 's/".*//g'`

RESPONSE=`curl -H "Authorization: bearer $TOKEN" ${ALPHA_SERVER}/collections/api/v01`
EXPECTED_RESULT="Hello from collections api"

echo ""
echo "Response from ${ALPHA_SERVER} is \"$RESPONSE\" "
[ "$RESPONSE" == "$EXPECTED_RESULT" ] && echo "TRUE : correct response " || echo "FALSE: wrong response"
echo ""

exit 0
