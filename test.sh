OUTPUT=""
COUNT=1
for run in {1..100}
do
  OUTPUT=""
  OUTPUT=$(java Main | grep -ic "(exception|error)")
  if [ $OUTPUT != 0 ]; then
    echo "Simulation #${COUNT} of 100 Failed!"
  else
    echo "Simulation #${COUNT} of 100 Passed."
  fi
  COUNT=$((COUNT+1))
done