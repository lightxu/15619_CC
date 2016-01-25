import requests
import os

# make sure you configure these three variables correctly before you try to run the code 
AZURE_ENDPOINT_URL='https://login.microsoftonline.com/04818ac6-becd-4259-ac10-841a9ba0fbe6/oauth2/token'
AZURE_APP_ID='40b1bc0d-08ec-4177-9210-a880e333aaa5'
AZURE_APP_SECRET='123456'

def get_token_from_client_credentials(endpoint, client_id, client_secret):
    payload = {
        'grant_type': 'client_credentials',
        'client_id': client_id,
        'client_secret': client_secret,
        'resource': 'https://management.core.windows.net/',
    }
    response = requests.post(endpoint, data=payload).json()
    return response['access_token']

# test
if __name__ == '__main__':
    auth_token = get_token_from_client_credentials(endpoint=AZURE_ENDPOINT_URL,
            client_id=AZURE_APP_ID,
            client_secret=AZURE_APP_SECRET)

    print auth_token