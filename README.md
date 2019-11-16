# Algorithm Practice

InterviewQuestions contains questions i have been asked in coding interviews.

## 1.

Servers are distributed across a 2d grid, with values 1 or 0. 1 meaning the server has a file and 0 meaning it doesnt.  Every hour the servers can send the file up, down, left, and right to other servers in the grid. Given a 2d grid of servers with the file in random coordinates, how long will it take to distribute the files to all the servers. input => howLong(grid, rows, cols); 

EX:
10000
00001
00100
hour 1
11001
10111
01111
hour 2
11111
11111
11111
2 hours to distribute the files


## 2.

given a list of ints, find the k most frequent

k = 2
list = {1,1,1,2,5,5,5}
kMostFrequent(k, list);
output = [1,5];

## 3.

given a string of brackets parentheses and braces determine if they all close or not.

# ex:

input = "{}"
output = valid since the brackets are closed properly

input = "[[[[{}]]]]"
output = valid since they are all closed

input = "}}{{"
output = not valid order matters

input = "{{(}}"
output not valid since parentheses isnt closed