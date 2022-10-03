from RPLCD.i2c import CharLCD
import sys
#implementació de les llibreries

def main():
	lcd = CharLCD(i2c_expander='PCF8574', address=0x27, port=1,
				cols=20, rows=4, dotsize=8,
				charmap='A02',
				auto_linebreaks=True,
				backlight_enabled=True) #inicialització de la LCD

	lcd.write_string("Escriu un text de un\n\rmaxim de 20x4 i prem\n\ renter i Ctrl+D per\n\racabar ")

	frase = sys.stdin.readlines() #llegim el string multilinia
	lcd.write_multiline(frase)#Utilitzem la funció creada per fer el string multilinia 

if __name__ == '__main__':
      main()