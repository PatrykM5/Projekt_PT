from turtle import *
import socket
import threading
import wx


gniazdko = 0;
tab_ap = []
tab_promieni = []
screen = ""
i=0
xclick = 0
yclick = 0
pos = []
constant_pos = []

class MyFrame(wx.Frame):
    def __init__(self):
        wx.Frame.__init__(self, None, wx.ID_ANY, 'WiFiLocator',pos=(300, 150), size=(500, 300))
        self.panel1 = wx.Panel(self, -1)
        self.button1 = wx.Button(self.panel1,label="Choose image", id=-1,pos=(10, 20), size = (100,25))
        self.button1.Bind(wx.EVT_BUTTON, self.loadFile)
        self.Show(True)



    def loadFile(self, event):
        global screen
        openFileDialog = wx.FileDialog(self, "Open", "", "","Gif files (*.gif)|*.gif",wx.FD_OPEN | wx.FD_FILE_MUST_EXIST)
        openFileDialog.ShowModal()
        path = openFileDialog.GetPath()
        screen = Screen()
        screen.bgpic(path)
        screen.title("WiFiLocator")
        screen.setup(800, 600)
        draw()


def getcoordinates():
    onscreenclick(modifyglobalvariables)


def modifyglobalvariables(rawx,rawy):
    global xclick
    global yclick
    global pos
    global i
    xclick = int(rawx//1)
    yclick = int(rawy//1)
    penup()
    cor = xclick,yclick
    pos.append(cor)
    constant_pos.append(cor)
    i = i + 1

def modify2():
    global pos
    global constant_pos
    global i
    global tab_promieni

    i=0
    print pos
    for y in pos:
        b = list(y)
        b[1] = (constant_pos[i])[1]-float(tab_promieni[i])
        y = tuple(b)
        pos[i] = y

        i = i + 1
    print pos
    i=0
    for x in pos:
        x
        penup()
        setpos(x)
        pendown()
        write("AP_" + str(tab_ap[i]))
        global tab_promieni
        circle(float(tab_promieni[i]))
        i = i + 1

def get_data():
    global i

    gniazdko = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    gniazdko.bind(("192.168.43.110",1024))
    print "Nasluchiwanie klientow..."
    gniazdko.listen(5)
    conn, addr = gniazdko.accept()
    print "Klient podlaczony!"

    dane = conn.recv(1024)
    dane = dane.split(':')
    dane = dane[1:]

    for dane_ap in dane:
        nazwa_ap = dane_ap.split(",")[0]
        promien_ap = dane_ap.split(",")[1]
        tab_ap.append(nazwa_ap)
        tab_promieni.append(promien_ap)

    print tab_ap
    print tab_promieni

    while True:
        global tab_promieni
        if(i==len(tab_promieni)):
            clear()
            modify2()
            dane_odl = conn.recv(1024)
            dane_odl = dane_odl.split(':')
            dane_odl = dane_odl[1:]

            tab_promieni = dane_odl
            print tab_promieni



def draw():
    raw_input("Dane otrzymane. Rysowanie zasiegow")
    color('red','yellow')
    getcoordinates()
    speed(0)
    screen.listen()
    mainloop()


t = threading.Thread(target=get_data)
t.start()

application = wx.PySimpleApp()
window = MyFrame()
application.MainLoop()









