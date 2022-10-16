import gi

gi.require_version("Gtk", "3.0")
from puzzle1 import LCD
from gi.repository import Gtk, Pango



class TextViewWindow(Gtk.Window):
    def __init__(self):
        Gtk.Window.__init__(self, title="TextView Example")

        self.set_default_size(400, 350)

        self.box = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=6)

        self.add(self.box)

        self.create_textview()
        self.create_toolbar()

    def create_button(self):
  

        button_clear = Gtk.ToolButton(label="Mostrar en LCD")
        button_clear.connect("clicked", self.on_print_clicked)
        self.box.pack_start(button_clear,True,True,0)

    def create_textview(self):
 

        self.textview = Gtk.TextView()
        self.textbuffer = self.textview.get_buffer()
        self.add(self.textview)

    def on_print_clicked(self, widget):
        l=LCD()
        start = self.textbuffer.get_start_iter()
        end = self.textbuffer.get_end_iter()
        l.lcd_multiline(self.textbuffer.get_text(start,end,0))


win = TextViewWindow()
win.connect("destroy", Gtk.main_quit)
win.show_all()
Gtk.main()
