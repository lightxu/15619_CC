import sys

filename = sys.argv[1]
total_request = 0
with open(filename, "r") as fin:
	for line in fin:
		items = line.split()
		total_request = total_request + int(items[-2])
print total_request
