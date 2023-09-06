# Windows
for /l %i in (1,1,30000) do echo "This is line %i" >> myfile.txt

# Linux
for i in {1..30000}; do echo "This is line $i"; done > myfile.txt