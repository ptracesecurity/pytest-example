# Używamy oficjalnego obrazu Pythona jako obrazu bazowego
FROM python:3.11
#
## Kopiujemy pliki wymagane do uruchomienia testów
COPY . /code

WORKDIR /code

# Instalujemy wymagane pakiety
RUN pip install --no-cache-dir -r requirements.txt
RUN ls
# Uruchamiamy pytest
CMD pytest --color=yes; tail -f /dev/null
