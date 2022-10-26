import gi

gi.require_version("Gtk", "3.0")
from puzzle1 import LCD
from gi.repository import Gtk, Pango, Gdk
import time



class TextViewWindow(Gtk.Window):
    def __init__(self):
        Gtk.Window.__init__(self, title="Puzzle 2 Bernat")
        self.set_resizable(False)
        self.set_size_request(364,170)
        self.box1 = Gtk.Box(orientation="vertical", spacing=4)
        self.add(self.box1)

        
        self.textview = Gtk.TextView()
        self.textbuffer = self.textview.get_buffer()
        self.box1.pack_start(self.textview,True,True,0)
        
                

        button = Gtk.Button(label="Display")
        button.connect("clicked", self.on_print_clicked)
        button.set_name("button")
        self.box1.pack_start(button,False,False,0)
        
        self.blue = b"""
        
        #button{
            background-color:#29c8f6;
            font-family:monospace;
            color: #FFFFFF;
            }
            
        textview{
            font:  30px monospace;
         }
            
        """
        
        self.blue2 = b"""
        
        #button{
            background-color: #d5f6f7;
            font-family:monospace;
            color: #FFFFFF;
            }
        """
            
        
        self.css_provider = Gtk.CssProvider()
        self.css_provider.load_from_data(self.blue)
        self.context = Gtk.StyleContext()
        self.screen = Gdk.Screen.get_default()
        self.context.add_provider_for_screen(self.screen, self.css_provider , Gtk.STYLE_PROVIDER_PRIORITY_APPLICATION)


    def on_print_clicked(self, widget):
        l=LCD()
        start = self.textbuffer.get_start_iter()
        end = self.textbuffer.get_end_iter()
        l.lcd_multiline(self.textbuffer.get_text(start,end,0))
        self.textbuffer.set_text("")

        
        
    


win = TextViewWindow()
win.connect("destroy", Gtk.main_quit)
win.show_all()
Gtk.main()
