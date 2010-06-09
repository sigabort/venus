#!/bin/sh

count=0

while [ true ] 
do

n=`mvn clean install`
if [ $? -eq 0 ]; then
    echo "$count: success"
else
    echo "$count: fail"
fi
count=$((count+1))
done
