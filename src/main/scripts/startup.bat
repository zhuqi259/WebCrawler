@echo off
if not exist log md log
java -jar WebCrawler.jar > log\output.log