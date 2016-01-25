import sys

filename = sys.argv[1]
view_count = 0
with open(filename, "r") as fin:
	for line in fin:
		# print line
		view = int(line.split()[1])
		if (view > 2500 and view < 3000):
			view_count = view_count + 1
print view_count
