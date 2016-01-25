import sys

filename = sys.argv[1]
total_request = 0
with open(filename, "r") as fin:
	for line in fin:
		items = line.split()
		if (len(items[0]) >= 2 and items[0][0].isdigit() and items[0][1].isalpha()):
			# print line
			total_request = total_request + int(items[1])
print total_request
