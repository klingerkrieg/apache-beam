import random
from time import time, mktime
from datetime import datetime

#user1_team1,team1,18,1447686663000,2015-11-16 15:11:03.921

users = [{"user":"joao","team":"team1"},
         {"user":"pedro","team":"team2"},
         {"user":"maria","team":"team3"},
         {"user":"jose","team":"team3"}]


datas = ["18/06/2017","19/06/2017","20/06/2017"]


def gerar_arquivo(date):
    date_day = datetime.strptime(date,"%d/%m/%Y")
    ts = int(mktime(date_day.timetuple()))

    fname = str(date_day).split(" ")[0]
    f = open(fname+".csv","w")

    i = 0
    while i < 10:
        
        usr_id = random.randint(0, len(users)-1)
        
        ts += random.randint(-40000,40000)
        date = datetime.fromtimestamp(ts)
        if (date_day.day != date.day):
            continue
        
        ln = users[usr_id]["user"]+","+users[usr_id]["team"]
        ln += ","+str(random.randint(0,100))
        ln += ","+str(ts)
        ln += ","+str(date)+"\n"
        f.write(ln)
        i += 1
        print ln


    f.close()



if __name__ == "__main__":
    
    for data in datas:
        gerar_arquivo(data)
