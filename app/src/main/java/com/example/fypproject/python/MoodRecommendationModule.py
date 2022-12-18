from os.path import dirname,join

import pandas as pd
from sklearn.cluster import KMeans
from sklearn.preprocessing import MinMaxScaler
import random

filename = join(dirname(__file__),"song_data.csv")

data = pd.read_csv(filename)

data.drop_duplicates(inplace=True,subset=['song_name'])
name=data['song_name']

col_features = ['danceability', 'energy', 'audio_valence', 'loudness']
X = MinMaxScaler().fit_transform(data[col_features])
kmeans = KMeans(init="k-means++",
                n_clusters=5,
                random_state=15).fit(X)
data['kmeans'] = kmeans.labels_

data

cluster=data.groupby(by=data['kmeans'])

y= data.pop('kmeans')
x= data.drop(['song_name'],axis=1)


from sklearn.model_selection import train_test_split
x_train,x_test,y_train,y_test=train_test_split(x,y,test_size=0.20)

from sklearn.neighbors import KNeighborsClassifier
knn = KNeighborsClassifier(n_neighbors = 5)
model=knn.fit(x_train,y_train)

# import lightgbm as ltb
# model=ltb.LGBMClassifier().fit(x_train,y_train)


model.score(x_train,y_train)

model.score(x_test,y_test)

df=cluster.apply(lambda x: x.sort_values(["song_popularity"],ascending=False))
df.reset_index(level=0, inplace=True)

isMusic = False

def recomm_music(emotion):
    NUM_RECOMMEND=20
    if emotion == 'verySad':
        # happy_set.append(df[df['kmeans']==0]['song_title'].head(NUM_RECOMMEND))
        # return happy_set
        return "Listen Music : " + df[df['kmeans']==0]['song_name'].head(NUM_RECOMMEND).sample().to_string(index = False)
    elif emotion == 'sad':
        return "Listen Music : " + df[df['kmeans']==1]['song_name'].head(NUM_RECOMMEND).sample().to_string(index = False)
    elif emotion == 'normal':
        return "Listen Music : " + df[df['kmeans']==2]['song_name'].head(NUM_RECOMMEND).sample().to_string(index = False)
    elif emotion == 'happy':
        return "Listen Music : " + df[df['kmeans']==3]['song_name'].head(NUM_RECOMMEND).sample().to_string(index = False)
    elif emotion == 'veryHappy':
        return "Listen Music : " + df[df['kmeans']==4]['song_name'].head(NUM_RECOMMEND).sample().to_string(index = False)

##########################################################################################################################
from bs4 import BeautifulSoup as SOUP
import re
import requests as HTTP

def recomm_movie(emotion):

    url = ""
    if (emotion == "sad"):
        url = 'http://www.imdb.com/search/title?genres=drama&title_type=feature&sort=moviemeter, asc'
    elif(emotion == "verySad"):
        url = 'http://www.imdb.com/search/title?genres=musical&title_type=feature&sort=moviemeter, asc'
    elif(emotion == "normal"):
        url = 'http://www.imdb.com/search/title?genres=thriller&title_type=feature&sort=moviemeter, asc'
    elif(emotion == "happy"):
        url = 'http://www.imdb.com/search/title?genres=western&title_type=feature&sort=moviemeter, asc'
    elif(emotion == "veryHappy"):
        url = 'http://www.imdb.com/search/title?genres=film_noir&title_type=feature&sort=moviemeter, asc'

    movies = []

    try:
        if not url:
            return movies
        response = HTTP.get(url)
        data = response.text
        soup = SOUP(data, "lxml")
        flags = ["None", "X", "\n"]
        for movieName in soup.findAll('a', attrs = {"href" : re.compile(r'\/title\/tt+\d*\/')}):
            movieName = str(movieName.string)
            if movieName not in flags:
                movies.append(movieName)

    except Exception as e:
        print(e)

    result = random.choice(movies)

    return "Watch movie : " + result


###############################################################################################################################
from faker import Faker
fake = Faker()

recomm_MusicTime = fake.date_between(start_date='today', end_date='+7d')

def recommMusic_day():
    day = recomm_MusicTime.strftime('%d')
    return day

def recommMusic_month():
    month = recomm_MusicTime.strftime('%m')
    return month

def recommMusic_year():
    year = recomm_MusicTime.strftime('%Y')
    return year

recomm_MovieTime = fake.date_between(start_date='today', end_date='+1M')

def recommMovie_day():
    day = recomm_MovieTime.strftime('%d')
    return day

def recommMovie_month():
    month = recomm_MovieTime.strftime('%m')
    return month

def recommMovie_year():
    year = recomm_MovieTime.strftime('%Y')
    return year