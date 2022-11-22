from RPLCD.i2c import CharLCD
import sys
lcd = CharLCD(i2c_expander='PCF8574', address=0x27, port=1, cols=20, rows=4, dotsize=8,charmap='A02', auto_linebreaks=True, backlight_enabled=True) 
class LCD: 
    def lcd_multiline(self,frase):  
        lcd.write_multiline(frase)
