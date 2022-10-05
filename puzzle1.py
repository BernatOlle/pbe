from RPLCD.i2c import CharLCD
import sys

class LCD: 
    def main(self):
        lcd = CharLCD(i2c_expander='PCF8574', address=0x27, port=1, cols=20, rows=4, dotsize=8,charmap='A02', auto_linebreaks=True, backlight_enabled=True) 
        lcd.write_string("Escriu un text de un\n\rmaxim de 20x4 i prem\n\renter i Ctrl+D per \n\racabar ")

        frase = sys.stdin.readlines()
        lcd.write_multiline(frase)

if __name__ == '__main__':
    l=LCD()
    l.main()
