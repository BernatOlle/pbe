import requests
url='http://localhost/server_uid.php'

string = "D70CCF64"

result=requests.get(url+'/'+string)
print(result.text)

url_1 ='http://localhost/conn.php'
string_1 = "tasks"
uid="D70CCF64";
result_1=requests.get(url_1+'/'+uid+'/'+string_1)
print(result_1.text)