from turtle import *
import socket
import threading

dlugosc = raw_input("Dlugosc pomieszczenia: ")
szerokosc = raw_input("Szerokosc pomieszczenia: ")
dlugosc = int(dlugosc)*10
szerokosc = int(szerokosc)*10

gniazdko = 0;
tab_ap = []
tab_promieni = []
screen = ""
i=0
xclick = 0
yclick = 0
pos = []

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
	setpos(xclick,yclick)
	pos.append(setpos)
        write("AP_"+str(tab_ap[i]))
	i = i + 1

def draw_ap_range():	
	for i in range (3):
		global pos
        	global tab_promieni
        	global i
		setpos(pos[i])
        	circle(int(tab_promieni[i]))
      

def create_screen(dlugosc, szerokosc):
	global screen 
	screen = Screen()
	screen.bgpic("poziom0.gif")
	screen.title("Welcome, Commadore.")
	screen.setup(800,600)
	#screen.setworldcoordinates(0,dlugosc*10,szerokosc*10,0)

def get_data():
	gniazdko = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
	gniazdko.bind(("127.0.0.1",1024))
	print "Nasluchiwanie klientow..."
	gniazdko.listen(5)
	conn, addr = gniazdko.accept()
	print "Klient podlaczony!"
	dane = conn.recv(1024)
	gniazdko.close()
	dane = dane.split(':')
	dane = dane[1:]
	for dane_ap in dane:		
		nazwa_ap = dane_ap.split(" ")[0]
		promien_ap = dane_ap.split(" ")[1]
		tab_ap.append(nazwa_ap)
		tab_promieni.append(promien_ap)

	print tab_ap
	print tab_promieni


def draw():
	raw_input("Dane otrzymane. Rysowanie zasiegow")
	color('red','yellow')      		
	getcoordinates()
	draw_ap_range()
	screen.listen()
	mainloop()	

listening = threading.Thread(target=get_data)
listening.start()
create_screen(dlugosc, szerokosc)
draw()

