import gi
gi.require_version("Gtk", "3.0")
from puzzle1 import Rfid_RC522
from gi.repository import GLib
import threading
from mfrc522 import *
reader_rfid= SimpleMFRC522()

class RfidReader(object):
    def _init_(self, rfid, handler):
        self.reader= rfid
        self.handler= handler

    def read_uid(self):
        t= threading.Thread(target=self.run)
        t.setDaemon(True)
        t.start()
    
    def run(self):
        uid= self.reader.read_uid(reader_rfid)
        GLib.idle_add(self.handler,self.reader, uid)