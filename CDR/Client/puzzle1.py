#!/usr/bin/env python
import os
import RPi.GPIO as GPIO
from mfrc522 import *

class Rfid_RC522:
    # return uid in hex str
    def read_uid(self):
        # define constructor
        reader= SimpleMFRC522()
        id= reader.read_id()
        id_hex= hex(id).upper()
        uid=id_hex[2:10]
        return uid

if __name__ == "__main__":
    try:
            print("Hold your card near the reader")
            rf= Rfid_RC522()
            uid= rf.read_uid()
            print("UID: %s\n" % (uid))
    except KeyboardInterrupt:
            GPIO.cleanup()
            raise
