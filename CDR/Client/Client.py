import gi
import threading
import requests
import json
from httprequest import httprequest
from rfidreader import RfidReader
from puzzle1 import Rfid_RC522
#from LCD import LCD

gi.require_version("Gtk", "3.0")
from gi.repository import Gtk
from gi.repository import Gdk 
from gi.repository import GLib
from threading import Timer, Thread
#l=LCD()
httprequest= httprequest()

class MyWindow(Gtk.Window):
    def _init_(self):
        window= super()._init_(title="window.py")
        self.set_default_size(600,500)
        self.set_border_width(10)
        
        self.DOMAIN1='http://169.254.207.20/pbe/server_uid.php'
        self.DOMAIN2='http://169.254.207.20/pbe/filtres.php'
        self.SESSION_TIME=300
        
        self.stack= Gtk.Stack()
        self.new_pantalla1()
        self.new_pantalla2()
        
        #self.pantalla1()
        self.stack.set_visible_child(self.grid1)
        self.add(self.stack)
        
        #Use CSS for style
        self.style_provider= Gtk.CssProvider()
        self.style_provider.load_from_path("/home/carlota/critical_review/style.css")
        self.context= Gtk.StyleContext()
        self.screen= Gdk.Screen.get_default()
        self.context.add_provider_for_screen(self.screen,self.style_provider,
                                                 Gtk.STYLE_PROVIDER_PRIORITY_APPLICATION)
    
    
    #Creates a new layout in a grid
    def new_pantalla1(self):
        # Create grid
        self.grid1= Gtk.Grid(column_homogeneous=True, row_homogeneous=True,
                             column_spacing=10,row_spacing=250)
        
        ##Create label with the right properties
        self.label= Gtk.Label(label="Please, login with your university card")
        self.label.set_justify(Gtk.Justification.CENTER)
        self.label.get_style_context().add_class('login_label')
        self.label.set_size_request(300,80)
        
        #Attach our label to the first screen
        self.grid1.attach(self.label,0,0,1,1)
        
        #Add our grid to the stack
        self.stack.add_named(self.grid1, "grid1")
        
    #Creates a new layout in a grid  
    def new_pantalla2(self):
        # Create grid
        self.grid2= Gtk.Grid(column_homogeneous=True, column_spacing=10,row_spacing=10)
        
        self.label_welcome=Gtk.Label()
        self.label_title= Gtk.Label()
        self.label_title.get_style_context().add_class('label_title')
        self.button_logout= Gtk.Button(label="Logout")
        self.button_logout.connect("clicked",self.logout)
        
        self.query = Gtk.Entry()
        self.query.set_placeholder_text("Insert query:")
        
        #Attach our label to the first screen
        self.grid2.attach(self.label_welcome,0,0,1,1)
        self.grid2.attach(self.button_logout,4,0,1,1)
        self.grid2.attach(self.query,0,1,5,1)
        self.grid2.attach(self.label_title,0,2,5,1)
        
        #Add our grid to the stack
        self.stack.add_named(self.grid2, "grid2")
        
    #Visualize pantalla1
    def pantalla1(self):
        self.stack.set_visible_child(self.grid1)
        #l.lcd_multiline('Please, login with \nyour university card')
        rfid.read_uid()
            
    #Visualize pantalla2
    def pantalla2(self):
        print(self.user)
        self.label_welcome.set_text("Welcome "+self.user)
        #l.lcd_multiline('Welcome\n '+self.user)
        self.query.connect("activate", self.on_query, self.uid)
        self.stack.set_visible_child(self.grid2)
        self.start_timer()

   #Dubte 
    def on_tag(self,reader,uid):
        self.uid = uid
        url=self.DOMAIN1+'/'+uid
        httprequest.get(url,self.on_unknown)

    #Dubte
    def on_unknown(self, user):
        self.user=user['Username']
        if self.user=="ERROR":
            self.dialog_error()
        else:
            self.pantalla2()
        
    def logout(self,widget=None):
        self.timer.cancel()
        self.grid2.remove_row(3)
        window.show_all()
        self.pantalla1()
        
    
    def dialog_error(self):
        dialog = Gtk.MessageDialog(
                name = "ERROR",
                transient_for=self,
                flags=0,
                message_type=Gtk.MessageType.ERROR,
                buttons=Gtk.ButtonsType.OK,
                text="UID NOT RECOGNIZED",
                )
        dialog.format_secondary_text("Sorry for the inconvenience. Please try again.")
        dialog.run()
        dialog.destroy()
        self.pantalla1()
    
    def on_query(self, query, uid):
        #x=[{"day":"Tue","hour":"08:00:00","subject":"TD","room":"A4-105"},{"day":"Tue","hour":"10:00:00","subject":"PSAVC","room":"A4-105"},{"day":"Tue","hour":"11:00:00","subject":"DSBM","room":"A4-105"},{"day":"Tue","hour":"12:00:00","subject":"RP","room":"A4-105"},{"day":"Wed","hour":"08:00:00","subject":"Lab PB","room":"C4-105"},{"day":"Thu","hour":"08:00:00","subject":"PBE","room":"A4-105"},{"day":"Thu","hour":"10:00:00","subject":"TD","room":"A4-105"}]
        #x=json.dumps([{'Name': '5', 'Age': '6'},{'Name': '10', 'Age': '14'}], separators=(',', ':'))
        
        text= query.get_text()
        query.set_text("");
        
        t=text.split("?")
        if t[0]=='timetables'or t[0]=='marks'or t[0]=='tasks':
            self.label_title.set_text(t[0])
        
        self.timer.cancel()
        self.start_timer()
        url= self.DOMAIN2+'/'+uid+'/'+text
        httprequest.get(url, self.on_result)
        print("Query")

  
    def on_result(self,table):
        #obtain the name of columns
        nom_col=[]
        print(table[0])
        for x in table[0].keys():
            nom_col.append(x)
    
        print(nom_col)
        if len(nom_col) == 4:
            self.tree= Gtk.ListStore(str,str,str,str)
            for x in table:
                print("Ha entrat")
                print(x)
                self.tree.append([x[nom_col[0]],x[nom_col[1]],x[nom_col[2]], x[nom_col[3]]])
        else:
            self.tree = Gtk.ListStore(str, str, str)
            for x in table:
                self.tree.append([x[nom_col[0]],x[nom_col[1]],x[nom_col[2]]])
        
        self.treeview = Gtk.TreeView.new_with_model(self.tree)         
            
        for i, column_title in enumerate(nom_col):   
            renderer = Gtk.CellRendererText()
            
            #Set the properties of the renderer and set the column titles
            renderer.set_fixed_size(150,40)
            renderer.set_property("xalign",0.5)      
            column = Gtk.TreeViewColumn(column_title,renderer,text=i)         
            column.set_alignment(0.5)           
            self.treeview.append_column(column)         
                 
            self.scrollable_treelist = Gtk.ScrolledWindow()      
            self.scrollable_treelist.set_vexpand(True)           
            self.grid2.attach(self.scrollable_treelist,0,3,5,5)      
            self.scrollable_treelist.add(self.treeview)
            window.show_all()

    
    def start_timer(self):
        self.timer = Timer(self.SESSION_TIME, self.logout)
        self.timer.setDaemon(True)                                               
        self.timer.start()
    
            
window= MyWindow()
window.connect("destroy", Gtk.main_quit)
rfid= RfidReader(Rfid_RC522(), window.on_tag)
rfid.read_uid()
window.show_all()
Gtk.main()
