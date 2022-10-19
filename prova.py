import gi

gi.require_version("Gtk", "3.0")
from puzzle1 import LCD
from gi.repository import Gtk, Pango, Gdk



class TextViewWindow(Gtk.Window):
    def __init__(self):
        Gtk.Window.__init__(self, title="MOSTRAR EN LCD")

        self.box = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=6)
        self.box.set_size_request(300, 150)
        self.add(self.box)
        self.css_button = b"""
        #button_clear{
            background-color: #2ac8f5;
            font-family: monospace;
        }
        
        #text{
            background-color: #2ac8f5;
            font-family: monospace;
        
        }
        label{
        
            background: #2ac8f5;
        }
        
        #pag_text{
            background: #2ac8f5
            
        }
        """

                
        self.provider = Gtk.CssProvider()
        #self.provider.load_from_data(self.css_button)
        self.context = Gtk.StyleContext()
        self.screen=Gdk.Screen.get_default()
        self.context.add_provider_for_screen(self.screen,self.provider,Gtk.STYLE_PROVIDER_PRIORITY_APPLICATION)
        
        
        self.create_textview()
        self.create_toolbar()

    def create_toolbar(self):
        box1 = Gtk.Box()
        
        button_clear = Gtk.Button(label="MOSTRAR")
        button_clear.connect("clicked", self.on_print_clicked)
        box1.pack_start(button_clear,True,True,0)
        box1.set_name("button_clear")
        self.provider.load_from_data(self.css_button)
        self.box.pack_start(box1, False,False,0)


    def create_textview(self):
        scrolledwindow = Gtk.ScrolledWindow()
        scrolledwindow.set_name("pag_text")
        self.box.pack_start(scrolledwindow, True,True,0)
        
        self.textview = Gtk.TextView()
        self.textview.set_name("text")
        self.textbuffer = self.textview.get_buffer()
        self.provider.load_from_data(self.css_button)
        scrolledwindow.add(self.textview)

    def on_print_clicked(self, widget):
        l=LCD()
        start = self.textbuffer.get_start_iter()
        end = self.textbuffer.get_end_iter()
        l.lcd_multiline(self.textbuffer.get_text(start,end,0))


win = TextViewWindow()
win.connect("destroy", Gtk.main_quit)
win.show_all()
Gtk.main()
