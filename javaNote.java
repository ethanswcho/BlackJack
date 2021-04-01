else {
            this.disableButtons();
            final Handler handler = new Handler(Looper.getMainLooper());

            // Delayed runnable for dealer.deal()
            Runnable delayedDeal = new Runnable(){
                @Override
                public void run(){
                    System.out.print("Doing loop!");
                    dealer.deal(deck.getACard());
                    // Until dealer's value exceeds hitting limit, keep hitting.
                    if(dealer.getValue() < dealer.getHittingLimit()){
                        handler.postDelayed(this, 1000);
                    }
                    else{
                        //Delayed runnable for delayedCheck()
                        Runnable dc = new Runnable(){
                            @Override
                            public void run(){
                                delayedCheck();
                            }
                        };
                        handler.postDelayed(dc, 1000);
                    }
                }
            };
            handler.postDelayed(delayedDeal, 500);
