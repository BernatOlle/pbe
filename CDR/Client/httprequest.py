import threading
import gi
gi.require_version("Gtk", "3.0")
from gi.repository import GLib
import requests

class httprequest:
    def get(self,url,handler):
        def run(url,handler):
            r = requests.get(url)                                   
            r.raise_for_status()
            json_response=r.json()
            print(json_response)
        
            GLib.idle_add(handler,json_response)
    
        t= threading.Thread(target=run, args=(url,handler))
        t.setDaemon(True)
        t.start()