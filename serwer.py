from turtle import *
import socket


gniazdko = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
gniazdko.bind(("192.168.43.110",1024))

dlugosc = raw_input("Dlugosc pomieszczenia: ")
szerokosc = raw_input("Szerokosc pomieszczenia: ")

print "Nasluchiwanie klientow..."
gniazdko.listen(1)
conn, addr = gniazdko.accept()
print "Klient podlaczony!"
raw_input("Narysowac graw?")

promien_zasiegu = conn.recv(1024)
print promien_zasiegu 

dlugosc = -200+int(dlugosc)*10
szerokosc = -200+int(szerokosc)*10

color('red','yellow')
begin_fill()


while True:
	penup()
        setpos(-200, -200)
	pendown()
	setpos(dlugosc,-200)
	pos_a = pos()	

	setpos(dlugosc,szerokosc)
	pos_b = pos()

	setpos(-200, szerokosc)
	pos_c = pos()

	setpos(-200,-200)
  	pos_d = pos()
      
        break

while True:
	penup()
	setpos(dlugosc,szerokosc-int(promien_zasiegu))
	write("AP_1")
	pendown()
	circle(int(promien_zasiegu))
	break

while True:
	penup()
        setpos(-200, szerokosc-int(promien_zasiegu))
	write("AP_2")
	pendown()
        circle(int(promien_zasiegu))
        break

while True:
	penup()
        setpos(dlugosc,-200-int(promien_zasiegu))
	write("AP_3")
	pendown()
        circle(int(promien_zasiegu))
        break

done()
                
