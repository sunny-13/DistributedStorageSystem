#!/bin/bash

#Make these port and configurations based on your local setup

echo "Starting MongoDB service..."
brew services start mongodb-community

echo "Starting MongoDB instances..."
mongod --port 27018 --dbpath ~/mongo/data1 --fork --logpath ~/mongo/data1/mongod.log
mongod --port 27019 --dbpath ~/mongo/data2 --fork --logpath ~/mongo/data2/mongod.log
mongod --port 27020 --dbpath ~/mongo/data3 --fork --logpath ~/mongo/data3/mongod.log
mongod --port 27021 --dbpath ~/mongo/data4 --fork --logpath ~/mongo/data4/mongod.log

echo "All MongoDB instances started successfully."

