import socket
import time
import os
import sys
import pandas as pd
import operator as op
import matplotlib.pyplot as plt
import numpy as np
import JihyeonDaemeori as JH

def Risk_Consumption(patternmonth):
    data = pd.read_csv('kookmin-out.csv', encoding='CP949')
    data = data.iloc[:-1]
    str_list = []
    count_list = {}
    for i in range(1, 13):
        globals()['data_{}'.format(i)] = pd.DataFrame(
            columns=['거래일시', '적요', '보낸분/받는분', '송금메모', '출금액', '입금액', '잔액', '거래점', '구분'])
        for j in range(0, len(data)):
            if int((list(data.iloc[j])[0].split('.'))[1]) == i and data.iloc[j, 1].replace('?', '') == '체크카드':
                globals()['data_{}'.format(i)] = globals()['data_{}'.format(i)].append(data.iloc[j])
        globals()['list_M{}'.format(i)] = list(globals()['data_{}'.format(i)].iloc[:, 2])
        for j in range(len(globals()['list_M{}'.format(i)])):
            try:
                globals()['list_M{}'.format(i)][j] = globals()['list_M{}'.format(i)][j].replace('?', '')
            except:
                pass
        globals()['count_{}'.format(i)] = {}
        for j in globals()['list_M{}'.format(i)]:
            try:
                globals()['count_{}'.format(i)][j] += 1
            except:
                globals()['count_{}'.format(i)][j] = 1
        globals()['count_{}'.format(i)] = sorted(globals()['count_{}'.format(i)].items(), key=op.itemgetter(1),reverse=True)
        for j in range(len(globals()['count_{}'.format(i)])):
            str_list.append(globals()['count_{}'.format(i)][j][0])
    for i in str_list:
        try:
            count_list[i] += 1
        except:
            count_list[i] = 1
    count_list = sorted(count_list.items(), key=op.itemgetter(1), reverse=True)
    pattern_list = []
    for i in range(len(count_list)):
        if count_list[i][1] >= patternmonth and count_list[i][0]:
            pattern_list.append(count_list[i][0])
    price_priority = []
    frequency_priority = []
    for i in pattern_list:
        out_sum = 0
        frequency_sum = 0
        for j in range(len(data)):
            try:
                if i == data.iloc[j, 2].replace('?', ''):
                    out_sum += int(data.iloc[j, 4].replace(',', ''))
                if i == data.iloc[j, 2].replace('?', ''):
                    frequency_sum += 1
            except:
                pass
        price_priority.append((i, out_sum))
        frequency_priority.append((i, frequency_sum))
    price_priority = sorted(price_priority, key=lambda x: x[1], reverse=True)
    frequency_priority = sorted(frequency_priority, key=lambda x: x[1], reverse=True)
    return_list = price_priority[0:5] + frequency_priority[0:5]
    return return_list

def Your_Want_Item(Itemprice,month):
    Consumption_list = []
    data = pd.read_csv('kookmin-out.csv', encoding='CP949')
    data = data.iloc[:-1]
    for i in range(1, 13):
        globals()['data_{}'.format(i)] = pd.DataFrame(columns=['거래일시', '적요', '보낸분/받는분', '송금메모', '출금액', '입금액', '잔액', '거래점', '구분'])
        for j in range(0, len(data)):
            if int((list(data.iloc[j])[0].split('.'))[1]) == i and data.iloc[j, 1].replace('?', '') == '체크카드':
                globals()['data_{}'.format(i)] = globals()['data_{}'.format(i)].append(data.iloc[j])
    for i in range(1, 13):
        globals()['list_M{}'.format(i)] = list(globals()['data_{}'.format(i)].iloc[:, 4])
        for j in range(len(globals()['list_M{}'.format(i)])):
            globals()['list_M{}'.format(i)][j] = int(globals()['list_M{}'.format(i)][j].replace(',', ''))
        Consumption_list.append(sum(globals()['list_M{}'.format(i)]))
    avg_Con = sum(Consumption_list) / 12
    Saving_percentage = round((100 - (((avg_Con - (Itemprice / month)) / avg_Con) * 100)), 3)
    # if Saving_percentage => 100:
    #     avg_con =  '*'

    return avg_Con, Saving_percentage

# print(str(Risk_Consumption(6)).replace(' ','').replace('[','').replace(']','').replace('(','').replace(')','').replace(',','/').replace('','/'))
# Risk_price = Risk_Consumption(6)[0]
# Risk_frequency = Risk_Consumption(6)[1]
# print('가격 입력')
# Itemprice = input('>>>')
# print('월 입력')
# month = input('>>>')
# Itemprice = int(Itemprice)
# month = int(month)
# print('월 평균 소비금액은 '+str(Your_Want_Item(Itemprice,month)[0])+'입니다')
# print(str(Itemprice)+'원 의 물건을 사기 위해서'+ ' 월 평균 '+str(Your_Want_Item(Itemprice,month)[1])+'%의 소비율을 줄여야 합니다')
# print('위험 소비 리포트')
# print(str(Your_Want_Item(Itemprice,month)[0])+'/'+str(Your_Want_Item(Itemprice,month)[1])+'/'+str(Risk_price)+'/'+str(Risk_frequency))
# print(Risk_frequency)
#
# x_rp = []
# y_rp = []
# x_rf = []
# y_rf = []
# x = np.arange(5)
# for i in range(len(Risk_price)):
#     x_rp.append(Risk_price[i][0])
#     y_rp.append(Risk_price[i][1])
#     x_rf.append(Risk_frequency[i][0])
#     y_rf.append(Risk_frequency[i][1])

# plt.bar(x, y_rp)
# plt.xticks(x, x_rp)
# plt.xlabel('')
# plt.show()
#
# plt.bar(x, y_rf)
# plt.xticks(x,x_rf)
# plt.show()
while True:
    try:
        host = ''
        port = 5505
        server_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_sock.bind((host, port))
        server_sock.listen(1)
        print('socket listen')
        client_sock, addr = server_sock.accept()
        print('Connected by', addr)
        while True:
            print("데이터 기다리는 중")
            data = client_sock.recv(1024)
            data = data.decode('utf-8')
            print(data)
            data = data.split('/')
            data[0] = data[0][-1]
            print(data)
            print(data[0]) # Risk_Consumption의 인자
            print(data[1]) # Your_Want_Item의 1번째 인자
            print(data[2]) # Your_Want_Item의 2번째 인자
            print(type(data[0]))  # Risk_Consumption의 인자
            print(type(data[1]))
            print(type(data[2]))
            # print(str(Risk_Consumption(int(data[0]))))
            # print(str(Your_Want_Item(int(data[1]), int(data[2]))[0]))
            # print(str(Your_Want_Item(int(data[1]), int(data[2]))[1]))
            # print(str(Risk_Consumption(int(data[0]))).replace(' ', '').replace('[', '').replace(']', '').replace('(','').replace(')', '').replace(',', '/'))
            Send_Data = str(Your_Want_Item(int(data[1]), int(data[2]))[0])+'/'+str(Your_Want_Item(int(data[1]), int(data[2]))[1])+'/'+str(Risk_Consumption(int(data[0]))).replace(' ', '').replace('[', '').replace(']', '').replace('(','').replace(')', '').replace(',', '/')
            print(Send_Data)
            # if data == '3번':
                # print(data)
                # client_sock.send(Send_Data.encode('utf-8'))
            # Send_data = str(Your_Want_Item(int(data[1])[1]), int(data[2]))[0] +'/'+str(Your_Want_Item(int(data[1])[1]), int(data[2]))[1]+'/'+str(Risk_Consumption(int(data[0]))).replace(' ','').replace('[','').replace(']','').replace('(','').replace(')','').replace(',','/').replace('','/')
            # client_sock.send('박지현 대머리'.encode('utf-8'))
            client_sock.send(Send_Data.encode('utf-8'))
            client_sock.send('stop'.encode('utf-8'))


                # if data == '3번':
                #     data = server_sock.recv(1024)
                #     data = data.decode('utf-8')
                #     data = data.split('/')
                #     Send_Data = Your_Want_Item(data[0],data[1])


                # print(data.decode("utf-8"))
                # server_sock.send(Send_Data.encode('utf-8'))
    except :
        continue


