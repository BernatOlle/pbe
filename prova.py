import gi

gi.require_version("Gtk", "3.0")
from gi.repository import Gtk, Pango



class TextViewWindow(Gtk.Window):
    def __init__(self):
        Gtk.Window.__init__(self, title="TextView Example")

        self.set_default_size(-1, 350)

        self.box = Gtk.Box()
        self.add(self.box)

        self.create_textview()
        self.create_toolbar()

    def create_toolbar(self):
        toolbar = Gtk.Toolbar()
        self.box.pack_start(toolbar, False,False,0)

        toolbar.insert(Gtk.SeparatorToolItem(), 8)

        button_clear = Gtk.ToolButton()
        button_clear.set_icon_name("edit-clear-symbolic")
        button_clear.connect("clicked", self.on_clear_clicked)
        toolbar.insert(button_clear, 9)

    def create_textview(self):
        scrolledwindow = Gtk.ScrolledWindow()
        scrolledwindow.set_hexpand(True)
        scrolledwindow.set_vexpand(True)
        self.box.pack_start(scrolledwindow, True,True,0)

        self.textview = Gtk.TextView()
        self.textbuffer = self.textview.get_buffer()
        scrolledwindow.add(self.textview)

    def on_clear_clicked(self, widget):
        start = self.textbuffer.get_start_iter()
        end = self.textbuffer.get_end_iter()
        print(self.textbuffer.get_text(start,end,0))


win = TextViewWindow()
win.connect("destroy", Gtk.main_quit)
win.show_all()
Gtk.main()
