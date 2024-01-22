# Używamy oficjalnego obrazu Pythona jako obrazu bazowego
FROM python:3.11
#
## Kopiujemy pliki wymagane do uruchomienia testów
COPY . /code

WORKDIR /code

# Instalujemy wymagane pakiety
RUN pip install -q --no-cache-dir -r requirements.txt

# Uruchamiamy pytest
CMD tail -f /dev/null
