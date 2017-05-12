
                
from turtle import *
import socket

gniazdko = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
gniazdko.bind(("127.0.0.1",1024))
print "Nasluchiwanie klientow..."
gniazdko.listen(5)
conn, addr = gniazdko.accept()
print "Klient podlaczony!"
dane = conn.recv(1024)
gniazdko.close()
dane = dane.split(':')
ap_1 = dane[1]
ap_2 = dane[2]
ap_3 = dane[3]
ap1_nazwa = ap_1.split(" ")[0]  
promien_zasiegu1 = ap_1.split(" ")[1]
ap2_nazwa = ap_2.split(" ")[0] 
promien_zasiegu2 = ap_2.split(" ")[1] 
ap3_nazwa = ap_3.split(" ")[0]
promien_zasiegu3 = ap_3.split(" ")[1]



dlugosc = raw_input("Dlugosc pomieszczenia: ")
szerokosc = raw_input("Szerokosc pomieszczenia: ")

 

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
	setpos(dlugosc,szerokosc-int(promien_zasiegu1))
	write(ap1_nazwa)
	pendown()
	circle(int(promien_zasiegu1))
	break

while True:
	penup()
        setpos(-200, szerokosc-int(promien_zasiegu2))
	write(ap2_nazwa)
	pendown()
        circle(int(promien_zasiegu2))
        break

while True:
	penup()
        setpos(dlugosc,-200-int(promien_zasiegu3))
	write(ap3_nazwa)
	pendown()
        circle(int(promien_zasiegu3))
        break

done()
