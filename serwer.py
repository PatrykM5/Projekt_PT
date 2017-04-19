import socket 
from threading import Thread 
from SocketServer import ThreadingMixIn 

class ClientThread(Thread): 
 
    def __init__(self,ip,port): 
        Thread.__init__(self) 
        self.ip = ip 
        self.port = port 
       
 
    def run(self): 
        while True : 
            data = conn.recv(2048) 
            print "Otrzymano nowe dane od uzytkownika...", data
            #MESSAGE = raw_input()
            if MESSAGE == 'exit':
                break
            conn.send(MESSAGE)  # echo 
 

TCP_IP = '0.0.0.0' 
TCP_PORT = 1024 
BUFFER_SIZE = 20  
 
tcpServer = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
tcpServer.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) 
tcpServer.bind((TCP_IP, TCP_PORT)) 
threads = [] 
 
while True: 
    tcpServer.listen(4) 
    print "Oczekiwanie na klientów..." 
    (conn, (ip,port)) = tcpServer.accept() 
    newthread = ClientThread(ip,port) 
    newthread.start() 
    threads.append(newthread) 
 
for t in threads: 
    t.join() 