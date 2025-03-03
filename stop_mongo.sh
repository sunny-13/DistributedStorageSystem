#!/bin/bash

#Make these port and configurations based on your local setup

echo "Stopping MongoDB service..."
brew services stop mongodb-community

echo "Stopping MongoDB instances..."
mongo --port 27018 --eval "db.shutdownServer()"
mongo --port 27019 --eval "db.shutdownServer()"
mongo --port 27020 --eval "db.shutdownServer()"
mongo --port 27021 --eval "db.shutdownServer()"

echo "All MongoDB instances stopped."
