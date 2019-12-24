FROM ubuntu:18.04
RUN apt-get update && apt-get install -y gcc g++ make flex bison ruby automake re2c default-jre
COPY LaViT2_8_9 /LaViT/